package duc.demo.service;


import duc.demo.dto.request.UserRequestDTO;
import duc.demo.dto.response.PageResponse;
import duc.demo.dto.response.UserDetailResponse;
import duc.demo.util.UserStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    long saveUser(UserRequestDTO requestDTO);

    void updateUser(long userId, UserRequestDTO requestDTO);
    void deleteUser(long userId);
    void changeStatus(long userId, UserStatus status);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUsersWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts);

    PageResponse<?>getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy );
    PageResponse<?>advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String address, String... search );

    PageResponse<?> advanceSearchBySpecification(Pageable pageable, String[] user, String[] address);
}
