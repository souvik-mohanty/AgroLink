import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../../components/Navbar/Navbar';
import ProductCard from '../../../components/ProductCard/ProductCard';
import { fetchAllProducts, addToCart } from '../../../service/productApi';
import './ProductPage.css';

const ProductPage = () => {
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();

  // ✅ Fetch products on mount
  useEffect(() => {
    fetchAllProducts()
      .then(res => setProducts(res.data))
      .catch(err => console.error('Failed to fetch products:', err));
  }, []);

  // ✅ Add product to cart
  const handleAddToCart = async (product) => {
    try {
      await addToCart(product.id, 1); // default quantity = 1
      alert(`✅ ${product.name} added to cart!`);
    } catch (error) {
      console.error('Add to cart failed:', error);
      alert('❌ Failed to add to cart.');
    }
  };

  // ✅ Navigate to checkout
  const handleBuyNow = (product) => {
    navigate(`/checkout/${product.id}`);
  };

  return (
    <>
      <Navbar variant="products" />
      <div className="product-page">
        <h2>Available Products</h2>
        <div className="product-grid">
          {products.length > 0 ? (
            products.map(product => (
              <ProductCard
                key={product.id}
                product={product}
                onAddToCart={handleAddToCart}
                onBuyNow={handleBuyNow}
              />
            ))
          ) : (
            <p>Loading products...</p>
          )}
        </div>
      </div>
    </>
  );
};

export default ProductPage;
