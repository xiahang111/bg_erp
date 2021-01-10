package com.bingo.erp.base.enums;

/**
 * 库存出入对照映射表
 */
public enum  StoreInOutMapperEnums {


    KANGDA("康达厂","康达料入库","出康达氧化"),
    DONGMEI("东美厂","东美料入库","出东美氧化"),
    FENGHE("风和厂","风和料入库","出风和氧化"),
    XIXING("铣型厂","","出铣型厂"),
    SANLIAN("三联喷涂厂","三联喷涂厂入库","出三联喷涂厂"),
    YUANMEI("原美喷涂厂","原美喷涂厂入库","出原美喷涂厂"),
    YIHE("亿和氧化厂","亿和氧化厂入库","出亿和氧化厂"),
    CHANGYUAN("长远拉丝厂","","出长远拉丝厂"),
    WAIXIAO("外销或发车间","","外销或发车间");


    public static String getNameByOutName(String outName){
        for (StoreInOutMapperEnums enums:StoreInOutMapperEnums.values()) {

            if (enums.outName.equals(outName)){
                return enums.name;
            }
        }

        return null;
    }

    public static String getInNameByName(String name){
        for (StoreInOutMapperEnums enums:StoreInOutMapperEnums.values()) {

            if (enums.name.equals(name)){
                return enums.inName;
            }
        }

        return null;
    }

    StoreInOutMapperEnums(String name,String inName,String outName){
        this.name = name;
        this.inName = inName;
        this.outName = outName;
    }

    private String name;

    private String inName;

    private String outName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(String inName) {
        this.inName = inName;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }
}
