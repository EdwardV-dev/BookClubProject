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