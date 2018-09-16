package com.sara_developers.android.newzfly.ModelClass;

import org.json.JSONObject;

import java.io.Serializable;

public class CmtJsonObjModel implements Serializable{

    JSONObject cmtObject;

    public JSONObject getCmtObject() {
        return cmtObject;
    }

    public CmtJsonObjModel(JSONObject cmtObject) {

        this.cmtObject = cmtObject;
    }
}
