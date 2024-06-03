const bcrypt = require('bcrypt');
const { FieldValue } = require('@google-cloud/firestore');
const { firestore } = require('../firebase');
const { generateToken } = require('../utils/auth');

const collectionRef = firestore.collection('users');

exports.register = async (req, res) => {
  const {
    name,
    email,
    password,
    username,
    gender,
    address,
    phone_number,
    image_url,
  } = req.body;

  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const newUser = await collectionRef.add({
      name,
      email,
      password: hashedPassword,
      username,
      gender,
      address,
      phone_number,
      image_url,
      createdAt: FieldValue.serverTimestamp(),
      updatedAt: FieldValue.serverTimestamp(),
    });

    const docRef = await newUser.get();
    const token = generateToken({ id: docRef.id, email });

    return res.status(201).json({
      status: 'success',
      message: 'User created successfully',
      data: { id: docRef.id, ...newUser },
      token,
    });
  } catch (error) {
    console.error('Error creating user:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.login = async (req, res) => {
  const { email, password } = req.body;

  try {
    const snapshot = await collectionRef.where('email', '==', email).get();
    if (snapshot.empty) {
      return res.status(404).json({
        status: 'error',
        message: 'Invalid email',
      });
    }

    const userDoc = snapshot.docs[0];
    const user = userDoc.data();

    const isPasswordMatch = await bcrypt.compare(password, user.password);
    if (!isPasswordMatch) {
      return res.status(400).json({
        status: 'error',
        message: 'Invalid password',
      });
    }

    const token = generateToken({ id: userDoc.id, email });

    return res.status(200).json({
      status: 'success',
      data: { id: userDoc.id, ...user },
      token,
    });
  } catch (error) {
    console.error('Error logging in user:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.getAll = async (req, res) => {
  try {
    const snapshot = await collectionRef.get();
    const users = snapshot.docs.map((doc) => ({
      id: doc.id,
      ...doc.data(),
    }));
    return res.status(200).json({
      status: 'success',
      data: users,
    });
  } catch (error) {
    console.error('Error fetching users:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.create = async (req, res) => {
  const {
    name,
    email,
    password,
    username,
    gender,
    address,
    phone_number,
    image_url,
  } = req.body;

  try {
    const docRef = await collectionRef.add({
      name,
      email,
      password,
      username,
      gender,
      address,
      phone_number,
      image_url,
      createdAt: FieldValue.serverTimestamp(),
      updatedAt: FieldValue.serverTimestamp(),
    });
    const newUser = {
      id: docRef.id,
      name,
      email,
      password,
      username,
      gender,
      address,
      phone_number,
      image_url,
    };

    return res.status(201).json({
      status: 'success',
      data: newUser,
    });
  } catch (error) {
    console.error('Error creating user:', error);
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
        message: 'User not found',
      });
    }

    return res.status(200).json({
      status: 'success',
      data: { id: doc.id, ...doc.data() },
    });
  } catch (error) {
    console.error('Error fetching user:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};

exports.update = async (req, res) => {
  const { id } = req.params;
  const {
    name,
    email,
    password,
    username,
    gender,
    address,
    phone_number,
    image_url,
  } = req.body;

  try {
    const docRef = collectionRef.doc(id);
    const doc = await docRef.get();
    if (!doc.exists) {
      return res.status(404).json({
        status: 'error',
        message: 'User not found',
      });
    }

    await docRef.update({
      name,
      email,
      password,
      username,
      gender,
      address,
      phone_number,
      image_url,
      updatedAt: FieldValue.serverTimestamp(),
    });

    return res.status(200).json({
      status: 'success',
      data: {
        id,
        name,
        email,
        password,
        username,
        gender,
        address,
        phone_number,
        image_url,
      },
    });
  } catch (error) {
    console.error('Error updating user:', error);
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
        message: 'User not found',
      });
    }

    await docRef.delete();

    return res.status(200).json({
      status: 'success',
      data: { id },
    });
  } catch (error) {
    console.error('Error deleting user:', error);
    return res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    });
  }
};
