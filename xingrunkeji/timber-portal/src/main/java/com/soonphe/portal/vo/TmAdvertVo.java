package com.soonphe.portal.vo;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  广告查询VO
 */
@Getter
@Setter
public class TmAdvertVo {

    @ApiParam(required = true, value = "页码")
    private int pageNum = 0;
    @ApiParam(required = true, value = "叶长")
    private int pageSize= 10;

    private int type;

    private int state;
}
