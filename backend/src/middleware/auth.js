const { verifytoken } = require('../utils/auth');

module.exports = async (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({
      status: 'error',
      message: 'Access denied, token is required',
    });
  }

  const verified = verifytoken(token);
  if (!verified) {
    return res.status(401).json({
      status: 'error',
      message: 'Access denied, invalid token',
    });
  }

  req.user = verified;
  next();
};
