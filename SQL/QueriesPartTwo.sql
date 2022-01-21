use `userbookstest`;

Select b.idBooks 
from books b 
Inner join authors au on au.idAuthor = b.idAuthor
where b.book_title = "Raiders" and au.author_first_name = "Henry" and au.author_last_name = "Smith";

Select *
from books;

SET @row_number = 0;  

Select * From 
                (Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name, au.author_last_name,
                (@row_number:=@row_number + 1) as row_num
                from books b
                Inner join authors au 
                on b.idAuthor = au.idAuthor) as innerTable
                where genre = "Fiction" and row_num = 1;
SET @row_number = 0; 
