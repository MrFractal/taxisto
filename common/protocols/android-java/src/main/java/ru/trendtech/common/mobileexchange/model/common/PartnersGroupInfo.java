package ru.trendtech.common.mobileexchange.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 19.09.2014.
 */
public class PartnersGroupInfo {
    private String section;
    private String groupName;
    private Long idGroup;
    private List<ItemPartnersGroupInfo> itemPartnersGroupInfo = new ArrayList<ItemPartnersGroupInfo>();


    public List<ItemPartnersGroupInfo> getItemPartnersGroupInfo() {
        return itemPartnersGroupInfo;
    }

    public void setItemPartnersGroupInfo(List<ItemPartnersGroupInfo> itemPartnersGroupInfo) {
        this.itemPartnersGroupInfo = itemPartnersGroupInfo;
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
