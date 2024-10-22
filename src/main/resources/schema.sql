-- CREATE TYPE status_enum AS ENUM ('ACTIVE','DEACTIVATED','EXPIRED'); --

-- Drop existing tables if they exist
DROP TABLE IF EXISTS campaign_users CASCADE;
DROP TABLE IF EXISTS invitations CASCADE;
DROP TABLE IF EXISTS notes CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS campaigns CASCADE;
DROP TABLE IF EXISTS users CASCADE;


create table campaign_users (campaign_id bigint not null, campaign_user_id bigint generated by default as identity, user_id bigint not null, campaign_role varchar(255) not null, screen_name varchar(255), primary key (campaign_user_id));
create table campaigns (campaign_id bigint generated by default as identity, description varchar(255), name varchar(255) not null, primary key (campaign_id));
create table invitations (campaign_id bigint not null, created_at timestamp(6), expires_at timestamp(6), invitation_id bigint generated by default as identity, token varchar(255), status_enum status_enum, primary key (invitation_id));
create table notes (created_at timestamp(6), note_id bigint generated by default as identity, parent_note_id bigint, session_id bigint, user_id bigint, text varchar(255) not null, user_role varchar(255), user_screen_name varchar(255), primary key (note_id));
create table sessions (session_date date not null, session_number integer, campaign_id bigint not null, session_id bigint generated by default as identity, primary key (session_id));
create table users (user_id bigint generated by default as identity, email varchar(255), password varchar(255) not null, role varchar(255) not null, username varchar(255) not null unique, primary key (user_id));
alter table if exists campaign_users add constraint FKkbgx37ayi1h4w82xs2w9fk1hc foreign key (user_id) references users;
alter table if exists campaign_users add constraint FKhsxkvianqxf5l0fojmx7efu9k foreign key (campaign_id) references campaigns;
alter table if exists invitations add constraint FKctwe5vc57t62jo6kn1j9yxgfm foreign key (campaign_id) references campaigns;
alter table if exists notes add constraint FKechaouoa6kus6k1dpix1u91c foreign key (user_id) references users;
alter table if exists notes add constraint FKq88h6mjmye505bifl284wpfoa foreign key (parent_note_id) references notes;
alter table if exists notes add constraint FKpwci0jamfvgygy3vqgqk0u8hp foreign key (session_id) references sessions;
alter table if exists sessions add constraint FKmdn9x1299h73m5k8s5v86g4yc foreign key (campaign_id) references campaigns;

ALTER TABLE notes ALTER COLUMN text TYPE text;