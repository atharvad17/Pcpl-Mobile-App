
package com.techinvest.pcpl.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnsignedReq implements Serializable {

    @SerializedName("jj")
    @Expose
    private List<Jj> jj = null;

    public List<Jj> getJj() {
        return jj;
    }

    public void setJj(List<Jj> jj) {
        this.jj = jj;
    }

}
