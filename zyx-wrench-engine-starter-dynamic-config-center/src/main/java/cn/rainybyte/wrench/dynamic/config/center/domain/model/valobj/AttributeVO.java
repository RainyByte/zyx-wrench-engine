package cn.rainybyte.wrench.dynamic.config.center.domain.model.valobj;

/**
 * 属性值调整对象
 */
public class AttributeVO {

    /** 键 - 属性 filename **/
    private String attribute;

    /** 值 **/
    private String value;

    public AttributeVO(){}

    public AttributeVO(String attribute, String value){
        this.attribute = attribute;
        this.value = value;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }
}
