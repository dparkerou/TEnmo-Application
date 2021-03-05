delete from transfers where transfer_id = 4;
delete from transfers where transfer_id = 1;

insert into transfers
(transfer_id, transfer_type_id, transfer_status_id,
account_from, account_to, amount) 
values(4, 1, 1, 1, 2, 50);

Update transfer_statuses
inner join
transfers 
on  transfers.transfer_status_id = transfer_statuses.transfer_status_id
set transfer_status = Approved, transfer_status_id = 5
where transfer_status_id = (select transfer_status_id from transfer where transfer_id = 4);

SELECT transfer_id, transfer_type_id, transfer_status_id,
account_from, account_to, amount 
FROM transfers
WHERE account_from = 1 or account_to = 1;

SELECT *
From transfers
inner join transfer_statuses
on transfer_statuses.transfer_status_id = transfers.transfer_status_id
inner join transfer_types
on transfer_types.transfer_type_id = transfers.transfer_type_id
where account_from = 1 or account_to = 1;

rollback;