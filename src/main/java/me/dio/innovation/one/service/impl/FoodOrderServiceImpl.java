package me.dio.innovation.one.service.impl;

import jakarta.transaction.Transactional;
import me.dio.innovation.one.controller.dto.AuthenticationSession;
import me.dio.innovation.one.domain.model.*;
import me.dio.innovation.one.domain.repository.FoodOrderRepository;
import me.dio.innovation.one.domain.repository.ShoppingCartRepository;
import me.dio.innovation.one.domain.repository.UserRepository;
import me.dio.innovation.one.service.FoodOrderService;
import me.dio.innovation.one.service.exception.BusinessException;
import me.dio.innovation.one.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodOrderServiceImpl implements FoodOrderService {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentGateway paymentGateway;

    @Transactional
    @Override
    public FoodOrder addFoodOrder(Long cartId, FoodOrder foodOrder) {
        // Verifica se o carrinho de compras existe
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseGet(() -> {
                    // Cria um novo carrinho de compras se não existir
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setId(cartId); // Supondo que o ID é definido externamente ou gerado
                    return shoppingCartRepository.save(newCart);
                });

        // Adiciona o pedido ao carrinho
        foodOrder.setShoppingCart(shoppingCart);
        return foodOrderRepository.save(foodOrder);
    }

    @Transactional
    @Override
    public FoodOrder updateFoodOrder(Long cartId, Long foodOrderId, FoodOrder foodOrder) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Shopping cart not found"));

        FoodOrder existingFoodOrder = foodOrderRepository.findById(foodOrderId)
                .orElseThrow(() -> new BusinessException("Food order not found"));

        // Verificar se o pedido pertence ao carrinho correto
        if (!existingFoodOrder.getShoppingCart().getId().equals(cartId)) {
            throw new BusinessException("Food order does not belong to this shopping cart");
        }

        // Atualizar os campos do pedido
        existingFoodOrder.setName(foodOrder.getName());
        existingFoodOrder.setIngredients(foodOrder.getIngredients());
        existingFoodOrder.setSize(foodOrder.getSize());
        existingFoodOrder.setQuantity(foodOrder.getQuantity());
        existingFoodOrder.setPrice(foodOrder.getPrice());
        existingFoodOrder.setSubtotal(foodOrder.getSubtotal());

        // Substituir a lista de additional_items
        existingFoodOrder.setAdditional_items(new ArrayList<>(foodOrder.getAdditional_items()));
        return foodOrderRepository.save(existingFoodOrder);
    }

    @Transactional
    @Override
    public void removeFoodOrder(Long cartId, Long foodOrderId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Shopping cart not found"));

        FoodOrder foodOrder = foodOrderRepository.findById(foodOrderId)
                .orElseThrow(() -> new BusinessException("Food order not found"));

        // Verificar se o pedido pertence ao carrinho correto
        if (!foodOrder.getShoppingCart().getId().equals(cartId)) {
            throw new BusinessException("Food order does not belong to this shopping cart");
        }

        // Remove o pedido da lista do carrinho
        shoppingCart.getFood_orders().remove(foodOrder);

        // Atualiza o carrinho no banco de dados
        shoppingCartRepository.save(shoppingCart);

        // Remove o pedido do banco de dados
        foodOrderRepository.delete(foodOrder);

    }

    @Transactional
    @Override
    public Total checkoutShoppingCart(Long cartId, String paymentMethod, Long userId) {
        // Buscar o usuário pelo ID fornecido
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        // Buscar o carrinho de compras pelo ID e garantir que pertence ao usuário autenticado
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));

        if (!shoppingCart.getUser().getId().equals(user.getId())) {
            throw new BusinessException("O carrinho de compras não pertence ao usuário autenticado");
        }

        // Exemplo de como acessar as informações do usuário
        String name = user.getName();
        String phoneNumber = user.getPhone_number();
        String cep = user.getCep();
        String address = user.getAddress();
        String neighborhood = user.getNeighborhood();
        String addressComplement = user.getAddress_complement();
        String city = user.getCity();


        // 1. Calcula o subtotal
        BigDecimal subtotal = shoppingCart.getFood_orders().stream()
                .map(FoodOrder::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Calcula taxas e descontos (implemente a lógica específica para o seu negócio)
        BigDecimal serviceFee = calculateServiceFee(subtotal);
        BigDecimal deliveryFee = calculateDeliveryFee(shoppingCart); // Pode depender do endereço, por exemplo
        BigDecimal discount = calculateDiscount(shoppingCart);
        BigDecimal incentives = calculateIncentives(shoppingCart);// Pode depender de cupons, promoções, etc.

        // 3. Calcula o total final
        BigDecimal finalTotal = subtotal
                .add(serviceFee)
                .add(deliveryFee)
                .subtract(discount);

        // 4. Cria ou atualiza o objeto Total
        Total total = shoppingCart.getTotal();
        if (total == null) {
            total = new Total();
            shoppingCart.setTotal(total);
        }
        total.setSubtotal(subtotal);
        total.setServiceFee(serviceFee);
        total.setDeliveryFee(deliveryFee);
        total.setDiscountRestaurant(discount);
        total.setIncentivesIfood(incentives);
        total.setFinalTotal(finalTotal);

        Payment payment = paymentGateway.processPayment(paymentMethod, shoppingCart.getTotal());

        shoppingCart.setPayment(payment);

        // 5. Salva o carrinho de compras com o total atualizado
        shoppingCartRepository.save(shoppingCart);

        return total;
    }

    @Override
    public List<FoodOrder> getFoodOrdersByCart(Long cartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("Shopping cart not found"));

        return foodOrderRepository.findByShoppingCart(shoppingCart);
    }

    // Métodos para calcular taxas e descontos
    private BigDecimal calculateServiceFee(BigDecimal subtotal) {
        // Exemplo: taxa de serviço de 10%
        return subtotal.multiply(BigDecimal.valueOf(0.10));
    }

    private BigDecimal calculateDeliveryFee(ShoppingCart shoppingCart) {
        // Exemplo: taxa de entrega fixa de R$ 5,00
        return BigDecimal.valueOf(5.00);
    }

    private BigDecimal calculateDiscount(ShoppingCart shoppingCart) {
        // Exemplo: desconto de R$ 2,00 para compras acima de R$ 30,00
        if (shoppingCart.getTotal().getSubtotal().compareTo(BigDecimal.valueOf(30.00)) >= 0) {
            return BigDecimal.valueOf(2.00);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateIncentives(ShoppingCart shoppingCart) {
        // Exemplo: incentivo de R$ 1,00 para cada item no carrinho
        return BigDecimal.valueOf(shoppingCart.getFood_orders().size());
    }
}
