alter table eg_sr_service  
add column if not exists servicetype character varying(256);


alter table eg_sr_service 
alter column servicecode drop not null ;