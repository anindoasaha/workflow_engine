package com.anindoasaha.workflowengine.prianza.bo;

import com.anindoasaha.workflowengine.prianza.util.IdGenerator;

import java.util.UUID;

public interface Entity {

    public String getId();

    public void setId(String id);

    public String getName();

    public void setName(String name);

    public default String getType() {
        return this.getClass().getCanonicalName();
    }

    static IdGenerator<String, String> identityGenerator = new IdGenerator<>(
            n -> n + "_" + UUID.randomUUID().toString());


}
