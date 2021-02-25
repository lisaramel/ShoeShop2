
-- add to cart

-- drop procedure addToCart;
delimiter //
CREATE PROCEDURE addToCart(VcustomerId int, VordersId int, VproductId int)
begin

    declare VordersIdCheck int default 0;
    declare current_value int;

	DECLARE EXIT HANDLER FOR 1264
    begin
	rollback;
	select ('Varan finns ej i lager, transaktionen avbryts') as error;
    END;

	DECLARE EXIT HANDLER FOR sqlexception
    begin
	rollback;
	select ('sqlexception, transaktionen avbryts') as error;
    END;
    
    start transaction;

    select count(*) from orders where id = VordersId group by id into VordersIdCheck;

    if VordersIdCheck = 0 then -- beställningen finns inte, ny beställning och produkten i
        insert into orders (dateOfOrder, customerId) values (CURRENT_TIMESTAMP, VcustomerId);
        insert into cart (ordersId, productId) values (last_insert_id(), VproductId);
    else -- beställningen finns, lägger till produkten
        insert into cart(ordersId, productId) values (VordersId, VproductId); 
    end if;
      -- minska lagerantalet
    select amountInStock from product where id = VproductId into current_value;
    update product set amountInStock = current_value -1 where id = VproductId;
    select 'procedur avslutad' as error;
    commit;

end //
delimiter ;


-- trigger hos produkt

delimiter //
create trigger After_Stock_Empty  
    after update
    on product
    for each row
begin 
	 declare _amountInStock int;
     select amountInStock from product where id = old.id into _amountInStock ;
	 if _amountInStock = 0 then 
		 set _amountInStock = 5;
		 insert into outOfStock(productId) values (OLD.id);
	 end if;
end //
delimiter ;


-- funktion för medelbetyg

delimiter //
create function get_avg_rate (VproductId int) returns double 
contains sql
begin 
 declare result double;
	select avg(rating.number) from product 
    left join review on product.id = review.productId
    left join rating on review.ratingId = rating.id
	where product.id = VproductId
	group by product.id into result;
    return result;
end //
delimiter ;


--  vy för medelbetyg, siffra och text

create view show_average_ratings as
select euSizing, colour, price, get_avg_rate(id),
CASE when get_avg_rate(id) = 1 then 'missnöjd'
when get_avg_rate(id) = 2 then 'ganska nöjd'
when get_avg_rate(id) = 3 then 'nöjd'
when get_avg_rate(id) = 4 then 'mycket nöjd'
end as rating_text 
from product;



--  procedure för rate

delimiter //
CREATE PROCEDURE rate(_customerId int, _productId int, _ratingNumber int , _ratingText varchar(3000) )
begin

	declare _ordersId int default -1;
    
    DECLARE EXIT HANDLER FOR sqlexception
    begin
	rollback;
	select ('Fel, transaktionen avbryts');
    END;
    
    start transaction; 
    
    select orders.id from orders
    join customer on orders.customerId = customer.id
    join cart on orders.id = cart.ordersId
	join product on cart.productId = product.id
    where customer.id = _customerId and product.id = _productId limit 1 into _ordersId;

	if _ordersId > 0 then
		insert into review (ratingId, text, productId, ordersId) VALUES (_ratingNumber, _ratingText, _productId, _ordersId);
	end if;
    
    commit;
    
end //
delimiter ;

