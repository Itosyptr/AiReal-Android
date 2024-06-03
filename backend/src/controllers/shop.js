const { FieldValue } = require('@google-cloud/firestore');
const { firestore } = require('../firebase');

const collectionRef = firestore.collection('shops');

exports.getAll = async (req, res) => {
  try {
    const snapshot = await collectionRef.get();
    const shops = snapshot.docs.map((doc) => ({
      id: doc.id,
      ...doc.data(),
    }));
    return res.status(200).json({
      status: 'success',
      data: shops,
    });
  } catch (error) {
    console.error('Error fetching shops:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.create = async (req, res) => {
  const { userId, name, description, street, city, province, image_url } =
    req.body;

  try {
    const docRef = await collectionRef.add({
      userId,
      name,
      description,
      street,
      city,
      province,
      image_url,
      createdAt: FieldValue.serverTimestamp(),
      updatedAt: FieldValue.serverTimestamp(),
    });
    const newShop = {
      id: docRef.id,
      userId,
      name,
      description,
      street,
      city,
      province,
      image_url,
    };

    return res.status(201).json({
      status: 'success',
      message: 'Shop created successfully',
      data: newShop,
    });
  } catch (error) {
    console.error('Error creating shop:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.getById = async (req, res) => {
  const { id } = req.params;

  try {
    const doc = await collectionRef.doc(id).get();
    if (!doc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'Shop not found',
      });
    }

    return res.status(200).json({
      status: 'success',
      data: { id: doc.id, ...doc.data() },
    });
  } catch (error) {
    console.error('Error fetching shop:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.update = async (req, res) => {
  const { id } = req.params;
  const { userId, name, description, street, city, province, image_url } =
    req.body;

  try {
    const docRef = collectionRef.doc(id);
    const doc = await docRef.get();
    if (!doc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'Shop not found',
      });
    }

    await docRef.update({
      userId,
      name,
      description,
      street,
      city,
      province,
      image_url,
      updatedAt: FieldValue.serverTimestamp(),
    });

    return res.status(200).json({
      status: 'success',
      message: 'Shop updated successfully',
    });
  } catch (error) {
    console.error('Error updating shop:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.delete = async (req, res) => {
  const { id } = req.params;

  try {
    const docRef = collectionRef.doc(id);
    const doc = await docRef.get();
    if (!doc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'Shop not found',
      });
    }

    await docRef.delete();

    return res.status(200).json({
      status: 'success',
      message: 'Shop deleted successfully',
    });
  } catch (error) {
    console.error('Error deleting shop:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};
