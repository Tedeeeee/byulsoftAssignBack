package project.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchConditionDTO {
    private String sortOrder;
    private String sortType;
    private int pageNumber;
    private String searchType;
    private String searchText;
}
