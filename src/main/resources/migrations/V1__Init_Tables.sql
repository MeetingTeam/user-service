create table friend_relation (id varchar(255) not null, status enum ('FRIEND','UNFRIEND') not null, friend1id varchar(255) not null, friend2id varchar(255) not null, primary key (id)) engine=InnoDB;
create table user (id varchar(255) not null, birthday date, email varchar(255) not null, gender bit, is_online bit, last_active datetime(6), nick_name varchar(255) not null, phone_number varchar(255), url_icon varchar(255), primary key (id)) engine=InnoDB;
alter table user add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);
alter table friend_relation add constraint FKmatnqyj8jw40dbbtot5r3sb5g foreign key (friend1id) references user (id);
alter table friend_relation add constraint FKgu2p55c56acoat5a722x7xfy3 foreign key (friend2id) references user (id);
