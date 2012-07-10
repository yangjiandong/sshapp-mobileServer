package org.ssh.pm.orm.hibernate;

@SuppressWarnings({ "deprecation"})
public class TrueFalseType extends org.hibernate.type.CharBooleanType {

    private static final long serialVersionUID = 2203653648284297849L;

    protected final String getTrueString() {
        return "t";
    }

    protected final String getFalseString() {
        return "f";
    }

    //example
    //@Column(name="indicator")
    //@Type(type="truefalse-type")
    //public Boolean getIndicator() {
    //	return indicator;
    //}
}
