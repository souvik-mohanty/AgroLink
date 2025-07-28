import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getProductById } from '../../../service/productApi';
import Navbar from '../../../components/Navbar/Navbar';
import './ProductDetails.css';

const ProductDetails = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loadingBuy, setLoadingBuy] = useState(false); 

  useEffect(() => {
    getProductById(id)
      .then(res => setProduct(res.data))
      .catch(err => console.error(err));
  }, [id]);

  const handleBuyNow = () => {
    setLoadingBuy(true);
    // Simulate API or navigation delay
    setTimeout(() => {
      setLoadingBuy(false);
      alert('Navigating to Buy Page...'); // You can navigate or call API here
    }, 2000);
  };

  if (!product) return <p className="loading-msg">Loading product details...</p>;

  return (
    <>
      <Navbar variant="products" />
      <div className="product-details">
        <div className="details-left">
          <img
            src={product.imageUrls[0]}
            alt={product.name}
            className="main-image"
            onError={(e) => e.target.style.display = 'none'}
          />
          <div className="image-gallery">
            {product.imageUrls.map((url, index) => (
              <img key={index} src={url} alt={`preview-${index}`} />
            ))}
          </div>
        </div>

        <div className="details-right">
          <h1>{product.name}</h1>
          <p><strong>Category:</strong> {product.category}</p>
          <p><strong>Price:</strong> ₹{product.pricePerUnit} / unit</p>
          <p><strong>Available Quantity:</strong> {product.quantityAvailable}</p>
          <p><strong>Quality:</strong> {product.qualityTag}</p>
          <p><strong>Crop Info:</strong> {product.cropInfo}</p>

          <div className="action-buttons">
            <button className="buy-btn" onClick={handleBuyNow} disabled={loadingBuy}>
              {loadingBuy ? 'Loading...' : 'Buy Now'}
            </button>
            <button className="cart-btn">Add to Cart</button>
          </div>
        </div>
      </div>
    </>
  );
};

export default ProductDetails;
