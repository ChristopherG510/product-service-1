package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.dto.SearchRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

            Object value;

            for(SearchRequestDto requestDto : searchRequestDto){

                if (Objects.equals(requestDto.getColumn(), "dateOrdered") && !(Objects.equals(requestDto.getOperation(), SearchRequestDto.Operation.FECHA_ENTRE))){
                    value = LocalDate.parse(requestDto.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else {
                    value = requestDto.getValue();
                }

                switch (requestDto.getOperation()){
                    case IGUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(requestDto.getColumn()), value);
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
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDto.getColumn()), requestDto.getValue());
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

                    case FECHA_ENTRE:
                        String[] split2 = requestDto.getValue().split(",");
                        Predicate dateBetween =criteriaBuilder.between(root.get(requestDto.getColumn()), LocalDate.parse(split2[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                LocalDate.parse(split2[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        predicates.add(dateBetween);
                        break;

                    case FECHA_DESPUES:
                        Predicate dateAfter = criteriaBuilder.greaterThan(root.get(requestDto.getColumn()),LocalDate.parse(requestDto.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        predicates.add(dateAfter);
                        break;

                    case FECHA_ANTES:
                        Predicate dateBefore = criteriaBuilder.lessThan(root.get(requestDto.getColumn()),LocalDate.parse(requestDto.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        predicates.add(dateBefore);
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
