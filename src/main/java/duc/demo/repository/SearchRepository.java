package duc.demo.repository;


import duc.demo.dto.response.PageResponse;
import duc.demo.model.User;
import duc.demo.repository.criteria.SearchCriteria;
import duc.demo.repository.criteria.UserSearchCriteriaConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository

public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy){
        //1. query ra list user
        StringBuilder sqlQuery = new StringBuilder("Select new duc.demo.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.email, u.phone) from User u where 1=1");
        if(StringUtils.hasLength(search)){
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");

        }
        if(StringUtils.hasLength(sortBy)){

            //firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                    sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
            }

        }


        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter("firstName", String.format("%%%s%%", search));
            selectQuery.setParameter("lastName", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }



//        System.out.println("SQL FINAL: " + sqlQuery.toString());
            List users = selectQuery.getResultList();



        System.out.println("users: " + users);



        //2. query ra so record theo dieu kien
        StringBuilder sqlCountQuery = new StringBuilder("Select count(u) from User u where 1=1");
        if(StringUtils.hasLength(search)){
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" or lower(u.email) like lower(?3)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if(StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println("totalElements: " + totalElements);


        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo, pageSize), totalElements);
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }
    public PageResponse<?> advanceSearchUser(int pageNo, int pageSize, String sortBy, String... search ){
        //firstName:T, lastName:T
        List<SearchCriteria> criteriaList = new ArrayList<>();

        //1. Lay ra danh sach user
        if(search != null){
            for(String s: search) {
                //firstName:value
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        //2. Lay ra so luong ban ghi va phan trang
        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy);

        Long totalElements = 1l;




        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(0)
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        //Xu li cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaConsumer queryConsumer = new UserSearchCriteriaConsumer(criteriaBuilder, predicate, root);
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        criteriaQuery.where(predicate);

        //sort
        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("desc")) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(columnName)));
                }else{
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(columnName)));

                }
            }
        }


        return entityManager.createQuery(criteriaQuery).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();




    }

}
