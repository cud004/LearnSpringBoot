package duc.demo.controller;

import duc.demo.configuration.Translator;
import duc.demo.dto.request.UserRequestDTO;
import duc.demo.dto.response.ResponseData;

import duc.demo.dto.response.ResponseError;
import duc.demo.dto.response.UserDetailResponse;
import duc.demo.exception.ResourceNotFoundException;
import duc.demo.service.UserService;
import duc.demo.util.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {



    private final UserService userService;


    @Operation(summary = "Add user", description = "API create new user")
    @PostMapping(value = "/")

    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user){
        log.info("Request add user, {} {} ",user.getFirstName(), user.getLastName());
        try{
         long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(),"User created successfully", userId);

        }catch(Exception e){
            log.error("errorMessage={}",e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");

        }

    }
    @Operation(summary = "Update user", description = "API update user")

    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) long userId, @Valid @RequestBody UserRequestDTO user) {
        log.info("Request update userId = {}", userId);
        try{
            userService.updateUser(userId, user);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.upd.success"), userId);

        }
        catch(Exception e){
            log.error("errorMessage={}",e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user failed");
        }

    }
    @Operation(summary = "Change status user", description = "API change status user")
    @PatchMapping("/{userId}")

    public ResponseData<?> changeStatus(@PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId, @RequestParam(required = false) UserStatus status){
        log.info("Request change status userId = {}", userId);
        try{
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Changed status successfully", userId);

        }
        catch(Exception e){
            log.error("errorMessage={}",e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Changed status failed");
        }
    }
    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}")

    public ResponseData<?> deleteUser(@PathVariable int userId){
        log.info("Request delete userId = {}", userId);
        try{
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted");
        }
        catch(Exception e){
            log.error("errorMessage={}",e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(),"Delete user failed");
        }
    }



    @Operation(summary = "Get one user", description = "API Get one user")
    @GetMapping("/{userId}")

    public ResponseData<UserDetailResponse>getUser(@PathVariable long userId){
        log.info("Request Get detail, userId = {}" ,userId);
        try{
            return new ResponseData<>(HttpStatus.OK.value(),"User", userService.getUser(userId));

        }
        catch (ResourceNotFoundException e){
            log.error("errorMessage={}",e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    //phaan trang
    @Operation(summary = "Get all users", description = "API Get all users")
    @GetMapping("/list")

    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0")
                                                                 int pageNo, @RequestParam(defaultValue = "10") @Min(10) int pageSize, @RequestParam(required = false) String sortBy){
        log.info("Request Get all user");

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsers(pageNo, pageSize, sortBy));

    }

    @Operation(summary = "Get all users with pageNo, pageSize, SortBy, PageResponse", description = "API Get all users")
    @GetMapping("/list-with-multiple-column")
    public ResponseData<?> getAllUsersWithSortByMultipleColumn(@RequestParam(defaultValue = "0")
                                                                        int pageNo, @RequestParam(defaultValue = "10") @Min(10) int pageSize, @RequestParam(required = false) String... sorts){
        log.info("Request Get all user with pageResponse");

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByMultipleColumn(pageNo, pageSize, sorts));

    }


    @Operation(summary = "Get all users with sort by column and search", description = "API Get all users with sort and search")
    @GetMapping("/list-with-sort-column-and-search")
    public ResponseData<?> getAllUsersWithSortByColumnAndSearch(@RequestParam(defaultValue = "0", required = false)
                                                               int pageNo, @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                @RequestParam(required = false) String search,
                                                                @RequestParam(required = false) String sortBy){
        log.info("Request Get all user with sort and search");

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy ));

    }

//Advance search By Criteria
    @Operation(summary = "Get all users with sort by column and search by criteria", description = "API Get all users with sort and search with criteria advance")
    @GetMapping("/advance-search-by-criteria")
    public ResponseData<?> advanceSearchByCriteria(@RequestParam(defaultValue = "0", required = false)
                                                                int pageNo, @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                @RequestParam(required = false) String sortBy,
                                                                @RequestParam(required = false) String address,
                                                                @RequestParam(required = false) String... search){
        log.info("Advance search with Criteria and paging and sorting");

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, address, search ));

    }
//Advance search By specification
    @Operation(summary = "Get all users with sort by column and search by specification", description = "API Get all users with sort and search with specification advance User and address")
    @GetMapping("/advance-search-by-specification")
    public ResponseData<?> advanceSearchBySpecification( Pageable pageable,
                                                   @RequestParam(required = false) String [] user,
                                                   @RequestParam(required = false) String [] address ){
        log.info("Advance search with specification and paging and sorting");

        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.advanceSearchBySpecification(pageable, user, address));

    }


    public UserService getUserService() {
        return userService;
    }
}
