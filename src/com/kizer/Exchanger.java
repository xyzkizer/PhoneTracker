package com.kizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Kizer
 * Date: 3/19/12
 * Time: 04:48 CET
 */


public class Exchanger {

    public static byte REQUEST = 1;
    public static byte RESPONSE = 2;

    private byte version;
    private byte type;
    private byte encode;
    private byte extend;
    private int command;
    private int length;

    private Map<String, String> values = new HashMap<String, String>();

    public Exchanger(byte version, byte type, byte encode, byte extend, int command) {
        this.encode = encode;
        this.command = command;
        this.extend = extend;
        this.type = type;
        this.version = version;
    }

    public String getValue(String key) {
        return values.get(key);
    }

    public void setValue(String key, String value) {
        values.put(key, value);
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public byte getExtend() {
        return extend;
    }

    public void setExtend(byte extend) {
        this.extend = extend;
    }

    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Exchanger{" + "values=" + values + ", version=" + version
                + ", type=" + type + ", encode=" + encode + ", extend=" + extend
                + ", command=" + command + ", length=" + length + '}';
    }
}