use `userbookstest`;

Select r.role_name, au.username
from app_user au
inner join app_role r on au.idRole = r.idRole;

Select * from app_role;

update app_user set idRole = (Select idRole from app_role where role_name = "ADMIN")
where app_user_id = 1 ;

Select * 
from books b
Inner join authors au on b.idAuthor = au.idAuthor;

select *
from app_user_has_books;

Select *
from authors;

Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor
from books b
Inner join authors au 
on b.idAuthor = au.idAuthor
where author_first_name = "JK" or author_last_name = "Smith";

Select r.role_name, au.username 
                from app_user au
                inner join app_role r on au.idRole = r.idRole
                where au.username = "Mozart5";
                
                
Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status, au.author_last_name, au.author_first_name
from books b
inner join app_user_has_books ab on ab.idBooks = b.idBooks
Inner join authors au on b.idAuthor = au.idAuthor
where ab.app_user_id=1; -- returns all books associated with a specific user


Select genre, COUNT(genre) as genreCount From books b
Inner join app_user_has_books ab on ab.idBooks = b.idbooks
Inner join app_user au on au.app_user_id = ab.app_user_id 
Where au.app_user_id=1 
GROUP BY genre
Order by genreCount desc
limit 1; -- fiction should be the result of this query. The result of this query is plugged into the query below via the "where clause"

Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name, au.author_last_name,
(Select Count(*) from books b where b.genre= "fiction" and b.approval_status = true) -- only one fiction book is approved
from books b
Inner join authors au 
on b.idAuthor = au.idAuthor
where b.genre= "fiction"
Order by RAND();


Select * From
(Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name, au.author_last_name,
ROW_NUMBER() OVER(Order by idBooks) as row_numbering
from books b
Inner join authors au 
on b.idAuthor = au.idAuthor) as innerTable
where genre= "fiction" and  row_numbering = 2;


Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name 
                from books b
                Inner join authors au 
                on b.idAuthor = au.idAuthor
                where au.author_first_name = "Henry" and au.author_last_name = "Smith" and b.book_title = "Fossils and more!";
 
 SET SQL_SAFE_UPDATES=0;
 update books set publication_year = 6000 where publication_year is null;
 SET SQL_SAFE_UPDATES=1;     
 
 insert into app_user_has_books (app_user_id, completion_status, idBooks)
 values
 (1, "WantToRead", 18);
 
 Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status, ab.app_user_id,
                 au.author_first_name, au.author_last_name from books b
                Inner join authors au 
                on b.idAuthor = au.idAuthor
                Inner join app_user_has_books ab on ab.idBooks = b.idBooks
                where (author_first_name = "JK" or author_last_name = "Rowling") and ab.app_user_id = 1;
