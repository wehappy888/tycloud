package org.tycloud.component.notification.face.orm.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.tycloud.component.notification.face.model.SmsVerifyModel;
import org.tycloud.component.notification.face.orm.entity.SmsVerify;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 验证码发送记录 Mapper 接口
 * </p>
 *
 * @author 子杨
 * @since 2017-09-09
 */
public interface SmsVerifyMapper extends BaseMapper<SmsVerify> {

    @Select(" SELECT SEQUENCE_NBR as sequenceNbr,SMS_CODE as smsCode,MOBILE as mobile,SMS_TYPE as smsType,SMS_VERIFY as smsVerify,SEND_TIME as sendTime,REC_DATE as recDate,REC_USER_ID as recUserId,REC_STATUS as recStatus FROM systemctl_sms_verify  WHERE SMS_TYPE=#{smsType} AND MOBILE=#{mobile}  ORDER BY SEND_TIME DESC  LIMIT 0,1 ")
    public SmsVerifyModel findByMobileAndSmsType(@Param("mobile") String mobile, @Param("smsType") String smsType);

}