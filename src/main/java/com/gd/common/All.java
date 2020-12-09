package com.gd.common;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * 校验所有规则，验证顺序：先校验默认分组，再校验指定了Edit的分组
 *
 * @author yff
 */
@GroupSequence({Default.class, Edit.class})
public interface All {
}
