package org.tycloud.component.snapshot.face.orm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.tycloud.component.snapshot.face.orm.entity.SnapshotGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 快照分组信息 Mapper 接口
 * </p>
 *
 * @author magintursh
 * @since 2017-08-25
 */
public interface SnapshotGroupMapper extends BaseMapper<SnapshotGroup> {





    /**
     * 根据主对象主键获取其最后一个快照对象
     * @param tableName
     * @param mainTypeSequenceNbr
     * @return
     */
    List<Map<String,Object>> getLastSnptEntity(String tableName, Long mainTypeSequenceNbr );




    /**
     * 根据主对象主键获取其最后一个快照对象
     * @param tableName
     * @param snptSequenceNbr
     * @return
     */
    Map getSnptEntity(String tableName, String snptSequenceNbr );

}