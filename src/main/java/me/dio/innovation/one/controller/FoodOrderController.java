package me.dio.innovation.one.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.innovation.one.controller.dto.FoodOrderDto;
import me.dio.innovation.one.domain.model.FoodOrder;
import me.dio.innovation.one.domain.model.Total;
import me.dio.innovation.one.service.FoodOrderService;
import me.dio.innovation.one.service.exception.BusinessException;
import me.dio.innovation.one.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/foodOrders")
@Tag(name = "Food Order Controller", description = "RESTful API for managing food orders")
public class FoodOrderController {

    @Autowired
    private FoodOrderService foodOrderService;

    @PostMapping("/{cartId}")
    @Operation(summary = "Add food order to shopping cart", description = "Add a new food order to the specified shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food order added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<FoodOrderDto> addFoodOrder(@PathVariable Long cartId, @RequestBody FoodOrderDto foodOrderDto) {
        FoodOrder foodOrder = foodOrderDto.toModel();
        FoodOrder createdOrder = foodOrderService.addFoodOrder(cartId, foodOrder);
        return ResponseEntity.status(201).body(new FoodOrderDto(createdOrder));
    }

    @PutMapping("/{cartId}/{foodOrderId}")
    @Operation(summary = "Update food order", description = "Update an existing food order in the specified shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Food order or shopping cart not found")
    })
    public ResponseEntity<FoodOrderDto> updateFoodOrder(@PathVariable Long cartId, @PathVariable Long foodOrderId, @RequestBody FoodOrderDto foodOrderDto) {
        FoodOrder foodOrder = foodOrderDto.toModel();
        FoodOrder updatedOrder = foodOrderService.updateFoodOrder(cartId, foodOrderId, foodOrder);
        return ResponseEntity.ok(new FoodOrderDto(updatedOrder));
    }

    @DeleteMapping("/{cartId}/{foodOrderId}")
    @Operation(summary = "Remove food order from shopping cart", description = "Remove a food order from the specified shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food order removed successfully"),
            @ApiResponse(responseCode = "404", description = "Food order or shopping cart not found")
    })
    public ResponseEntity<Void> removeFoodOrder(@PathVariable Long cartId, @PathVariable Long foodOrderId) {
        foodOrderService.removeFoodOrder(cartId, foodOrderId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<Total> checkoutShoppingCart(
            @PathVariable Long cartId,
            @RequestParam String paymentMethod,
            @RequestParam Long userId) {
        try {
            // Chama o serviço para realizar o checkout
            Total total = foodOrderService.checkoutShoppingCart(cartId, paymentMethod, userId);

            // Retorna a resposta com o total calculado
            return ResponseEntity.ok(total);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get food orders by shopping cart", description = "Retrieve all food orders in the specified shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found")
    })
    public ResponseEntity<List<FoodOrderDto>> getFoodOrdersByCart(@PathVariable Long cartId) {
        List<FoodOrder> orders = foodOrderService.getFoodOrdersByCart(cartId);
        return ResponseEntity.ok(FoodOrderDto.fromModelList(orders));
    }
}
