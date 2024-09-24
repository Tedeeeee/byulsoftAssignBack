package project.assign.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import project.assign.exception.BusinessExceptionHandler;

@Getter
@NoArgsConstructor
public class SearchConditionDTO {

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
        validatePageNumber(pageNumber);
        this.sortOrder = sortOrder;
        this.sortType = sortType;
        this.pageNumber = pageNumber;
        this.searchType = searchType;
        this.searchText = searchText;
        this.offset = getPageOffset();
    }

    private void validateSortOrder(String sortOrder) {
        if (!"asc".equals(sortOrder) && !"desc".equals(sortOrder)) {
            //정렬 순서는 'asc' 또는 'desc' 만 가능
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "검색 조건에 오류가 있습니다");
        }
    }

    private void validateSortType(String sortType) {
        if (!sortType.matches("^(difficulty|story|interior|horror|activity)$")) {
            //정렬 타입은 'difficulty', 'story', 'interior', 'horror', 'activity' 중 하나만 가능
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "검색 조건에 오류가 있습니다");
        }
    }

    private void validateSearchType(String searchType) {
        if (!"제목".equals(searchType) && !"지역".equals(searchType)) {
            //검색 타입은 '제목' 또는 '지역'만 가능
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "검색 조건에 오류가 있습니다");
        }
    }

    private void validatePageNumber(int pageNumber) {
        if (pageNumber < 1) {
            //페이지 번호는 1 이상의 숫자여야 합니다.
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "검색 조건에 오류가 있습니다");
        }
    }
}
