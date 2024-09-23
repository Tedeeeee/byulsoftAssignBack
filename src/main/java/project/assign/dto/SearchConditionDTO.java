package project.assign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchConditionDTO {

    // 각각의 Validation 을 만들어주어야 한다.
    private String sortOrder;
    private String sortType;
    private int pageNumber;
    private String searchType;
    private String searchText;
    public int offset;

    public int getPageOffset() {
        int pageSize = 5;
        return (pageNumber - 1) * pageSize;
    }

    public boolean isSortOrderEmpty() {
        return sortOrder.isEmpty();
    }

    public SearchConditionDTO(String sortOrder, String sortType, int pageNumber, String searchType, String searchText) {
        this.sortOrder = sortOrder;
        this.sortType = sortType;
        this.pageNumber = pageNumber;
        this.searchType = searchType;
        this.searchText = searchText;
        this.offset = getPageOffset();
    }
}
