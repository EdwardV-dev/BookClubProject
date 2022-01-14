use `userbookstest`;

Select b.idBooks 
from books b 
Inner join authors au on au.idAuthor = b.idAuthor
where b.book_title = "Raiders" and au.author_first_name = "Henry" and au.author_last_name = "Smith";
