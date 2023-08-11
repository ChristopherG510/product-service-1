package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.dto.SearchRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FilterSpecification<T> {

    public Specification<T> getSearchSpecification(SearchRequestDto searchRequestDto){

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(searchRequestDto.getColumn()), searchRequestDto.getValue());
            }
        };
    }

    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDto, RequestDto.GlobalOperator globalOperator){

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            for(SearchRequestDto requestDto : searchRequestDto){


                switch (requestDto.getOperation()){
                    case IGUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(requestDto.getColumn()), requestDto.getValue());
                        predicates.add(equal);
                        break;

                    case CONTIENE:
                        Predicate like = criteriaBuilder.like(root.get(requestDto.getColumn()),"%" + requestDto.getValue() + "%");
                        predicates.add(like);
                        break;

                    case EN:
                        String[] split = requestDto.getValue().split(",");
                        Predicate in = root.get(requestDto.getColumn()).in(Arrays.asList(split));
                        predicates.add(in);
                        break;

                    case MENOR_QUE:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDto.getColumn()),requestDto.getValue());
                        predicates.add(lessThan);
                        break;

                    case MAYOR_QUE:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(requestDto.getColumn()),requestDto.getValue());
                        predicates.add(greaterThan);
                        break;

                    case ENTRE:
                        String[] split1 = requestDto.getValue().split(",");
                        Predicate between = criteriaBuilder.between(root.get(requestDto.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                        predicates.add(between);
                        break;
//                    case JOIN:
//                        criteriaBuilder.equal(root.join("joinTable").get("attribute from join table"),requestDto.getValue());
//                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: ");
                }
            }

            if (globalOperator.equals(RequestDto.GlobalOperator.AND)){
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };

    }

}
