insert into ingredient (id,name) values (1,'Oeuf') on conflict (id) do update set name=excluded.name;
insert into ingredient (id,name) values (2,'Huile') on conflict (id) do update set name=excluded.name;
insert into ingredient (id,name) values (3,'Saucisse') on conflict (id) do update set name=excluded.name;
insert into ingredient (id,name) values (4,'Pain') on conflict (id) do update set name=excluded.name