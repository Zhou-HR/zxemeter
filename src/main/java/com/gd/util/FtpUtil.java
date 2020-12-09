package com.gd.util;

import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import com.gd.basic.crud.CrudService;
import com.gd.basic.crud.FilterRule;
import com.gd.model.po.SysCode;


/**
 * @author ZhouHR
 */
@Slf4j
public class FtpUtil {

    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;
    public static String PORT;
    public static String HEAD;

    static {
        CrudService crudService = CrudService.of(SysCode.class);
        FilterRule rules = new FilterRule("dicCode", "ftp");
        SysCode sysCode = crudService.find(rules);
        URL = sysCode.getDicKey();
        String str = sysCode.getDicValue();
        String arr[] = str.split(":");
        USERNAME = arr[0];
        PASSWORD = arr[1];
        PORT = arr[2];

        rules = new FilterRule("dicCode", "OUTER_IMG_URL");
        sysCode = crudService.find(rules);
        HEAD = sysCode.getDicValue();
    }


    public static void upload(String directoryName,
                              String serverfile, MultipartFile file) throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(URL, Integer.valueOf(PORT));
//			System.out.print(ftpClient.getReplyString());

            // check reply code.
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
//				System.out.println("Connection refused.");
                return;
            }

            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			System.out
//					.println("Workdir >>" + ftpClient.printWorkingDirectory());
//			ftpClient.makeDirectory(directoryName);
            makeDirectory(ftpClient, directoryName);
            ftpClient.changeWorkingDirectory(directoryName);
            ftpClient.storeFile(serverfile, file.getInputStream());

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception e) {

            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            log.error(e.getMessage());
            throw e;
            //e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
    }

    public static void upload(String directoryName,
                              String serverfile, InputStream is) throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(URL, Integer.valueOf(PORT));
//			System.out.print(ftpClient.getReplyString());

            // check reply code.
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
//				System.out.println("Connection refused.");
                return;
            }

            ftpClient.login(USERNAME, PASSWORD);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//			System.out
//					.println("Workdir >>" + ftpClient.printWorkingDirectory());
//			ftpClient.makeDirectory(directoryName);
            makeDirectory(ftpClient, directoryName);
            ftpClient.changeWorkingDirectory(directoryName);
            ftpClient.storeFile(serverfile, is);

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception e) {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            log.error(e.getMessage());

            throw e;
            //e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
    }


    public static void makeDirectory(FTPClient ftpClient, String directoryName) {
        if ("/".equals(directoryName.substring(0, 1)))
            directoryName = directoryName.substring(1);
        if ("/".equals(directoryName.substring(directoryName.length() - 1)))
            directoryName = directoryName.substring(0, directoryName.length() - 1);
        String[] arr = directoryName.split("/");
        String dir = "";
        for (String str : arr) {
            dir += "/" + str;
            try {
                ftpClient.makeDirectory(dir);
            } catch (IOException e) {
                //e.printStackTrace();
                log.error(e.getMessage());
            }
        }

    }
	/*
	public static void showPic(String path,HttpServletResponse response){
		int pos=path.lastIndexOf("/");
		
		String fileName=path.substring(pos+1,path.length());
		path=path.substring(0,pos);
		String directoryName="/"+path;
		
		FTPClient ftpClient = new FTPClient();
		OutputStream os=null;
		try {
			ftpClient.connect(URL);
			ftpClient.login(USERNAME, PASSWORD);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(directoryName);
			FTPFile[] fs = ftpClient.listFiles();
			os = response.getOutputStream();
			ftpClient.retrieveFile(fileName, os);
			os.flush();
			os.close();
	         
	        ftpClient.logout();  
			
		}catch(Exception e){
			if(os!=null){
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
			e.printStackTrace();
		}
		
	}

	public void useFTP(String ftpserver, String directoryName,
			String filetoUpload, String serverFileName, String username,
			String password) {
		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.connect(ftpserver);
			System.out.print(ftpClient.getReplyString());

			// check reply code.
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				ftpClient.disconnect();
				System.out.println("Connection refused.");
				return;
			}

			ftpClient.login(username, password);
			System.out
					.println("Workdir >>" + ftpClient.printWorkingDirectory());
			ftpClient.makeDirectory(directoryName);
			ftpClient.changeWorkingDirectory(directoryName);
			
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			// Store file
			FileInputStream input = new FileInputStream(filetoUpload);
			ftpClient.storeFile(serverFileName, input);

			// List all Files and directories
			ftpClient.enterLocalPassiveMode();
			FTPFile[] files = ftpClient.listFiles();

			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					// If file print name and size
					if (files[i].isFile()) {
						System.out.println("File >> " + files[i].getName()
								+ "\tSize >>" + files[i].getSize());
					}
				}
			}
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException e) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FtpUtil xx = new FtpUtil();
		xx.useFTP("127.0.0.1", "a1", "D:\\222.jpg", "222.jpg", "user",
				"123456");
		
	}
*/
}
