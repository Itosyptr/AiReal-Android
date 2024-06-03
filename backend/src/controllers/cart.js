const { FieldValue } = require('@google-cloud/firestore');
const { firestore } = require('../firebase');

const cartCollection = firestore.collection('carts');

exports.addToCart = async (req, res) => {
  const { userId, items } = req.body;

  try {
    const cartDoc = await cartCollection.doc(userId).get();
    let cart;

    if (!cartDoc.exists) {
      cart = {
        userId,
        items: [],
        createdAt: FieldValue.serverTimestamp(),
        updatedAt: FieldValue.serverTimestamp(),
      };
    } else {
      cart = cartDoc.data();
    }

    items.forEach((item) => {
      const existingItemIndex = cart.items.findIndex(
        (cartItem) => cartItem.productId === item.productId
      );
      if (existingItemIndex > -1) {
        cart.items[existingItemIndex].quantity += item.quantity;
      } else {
        cart.items.push(item);
      }
    });

    await cartCollection.doc(userId).set(cart, { merge: true });

    return res
      .status(200)
      .json({ status: 'success', message: 'Items added to cart', data: cart });
  } catch (error) {
    console.error('Error adding to cart:', error);
    return res
      .status(500)
      .json({ status: 'error', message: 'Internal Server Error' });
  }
};

exports.removeFromCart = async (req, res) => {
  const { userId, productId } = req.body;

  try {
    const cartDoc = await cartCollection.doc(userId).get();
    if (!cartDoc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'Cart not found',
      });
    }

    const cart = cartDoc.data();
    cart.items = cart.items.filter((item) => item.productId !== productId);

    await cartCollection.doc(userId).set(cart);
    return res.status(200).json({
      status: 'success',
      message: 'Product removed from cart successfully',
      data: cart,
    });
  } catch (error) {
    console.error('Error removing from cart:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.viewCart = async (req, res) => {
  const { userId } = req.query;

  try {
    const cartDoc = await cartCollection.doc(userId).get();
    if (!cartDoc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'Cart not found',
      });
    }

    const cart = cartDoc.data();
    return res.status(200).json({
      status: 'success',
      data: cart,
    });
  } catch (error) {
    console.error('Error viewing cart:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};
