package top.cuizilin.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装每个mapper.xml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMappedStatement {
    private String namespace;
    private String sqlId;
    private String sql;
    private String parameterType;
    private String resultType;
}
