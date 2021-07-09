
insert into users(id,first_name,last_name) values (0,'ilyas','Berdibekov');
insert into users(id,first_name,last_name) values (1,'Justine','Bibber');
insert into users(id,first_name,last_name) values (2,'Barak','Obama');
insert into users(id,first_name,last_name) values (3,'Michel','Jackson');

insert into projects(project_id,name,description) values (0,'Web site','Description');
insert into projects(project_id,name,description) values (1,'Poll System','Description');
insert into projects(project_id,name,description) values (2,'Note System','Description');

insert into tasks(task_id,project_id,user_id,status,name,description) values (0,0,0,'Open','1.Bug fix','Incorrect print');
insert into tasks(task_id,project_id,user_id,status,name,description) values (1,1,1,'Open','Add unit test','Add unit test');

insert into tasks(task_id,project_id,user_id,parent_task_id,status,name,description) values (3,1,1,0,'Open','1.1.Sub task Add unit test','Sub');
insert into tasks(task_id,project_id,user_id,parent_task_id,status,name,description) values (4,1,1,3,'Open','1.1.1.Sub Sub task Add unit test','Sub sub');



insert into node(id,parent_id,name ) values (0,null ,'A');
insert into node(id,parent_id,name ) values (1,0,'B sab A');
insert into node(id,parent_id,name ) values (2,null ,'C');

