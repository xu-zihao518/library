package org.example.library.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.library.pojo.Book;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from books")
    List<Book> list();
    @Insert("INSERT into books(bookId,bookISBN,bookName,author,publisher,publicationDate,category,count,description)"
            +"values(#{bookId},#{bookISBN},#{bookName},#{author},#{publisher},#{publicationDate},#{category},#{count},#{description})")
    void add(Book book);

    @Delete("Delete from books where bookId=#{bookId} and bookName=#{bookName}")
    void delete(Book book);
}
