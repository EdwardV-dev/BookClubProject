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

Select author_first_name, author_last_name
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
                
                
Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, ab.completion_status, au.author_last_name
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

Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name, au.author_last_name
from books b
Inner join authors au 
on b.idAuthor = au.idAuthor
where b.genre= "fiction"
Order by RAND()
Limit 1;


Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor, au.author_first_name 
                from books b
                Inner join authors au 
                on b.idAuthor = au.idAuthor
                where au.author_first_name = "Henry" and au.author_last_name = "Smith" and b.book_title = "Fossils and more!";
                
                
