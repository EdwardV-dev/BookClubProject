use `userbookstest`;

Select * from books;

Select author_first_name, author_last_name
from authors;

Select b.book_title, b.genre, b.idBooks, b.approval_status, b.publication_year, b.idAuthor
from books b
Inner join authors au 
on b.idAuthor = au.idAuthor
where author_first_name = "JK";