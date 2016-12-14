package com.online.factory.factoryonline.models.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.online.factory.factoryonline.models.SearchResult;

import java.util.List;

/**
 * 作者: GIndoc
 * 日期: 2016/12/12 11:56
 * 作用:
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class SearchResultResponse extends Response {
    private List<SearchResult> searchResult;

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }
}
