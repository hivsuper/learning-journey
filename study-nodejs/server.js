// server.js  
const express = require('express');  
const path = require('path');  

const app = express();  
const PORT = 3000;  

// 提供静态文件  
app.use(express.static(path.join(__dirname)));  

// 路由处理  
app.get('/', (req, res) => {  
    res.sendFile(path.join(__dirname, 'index.html'));  
});  

// 启动服务器  
app.listen(PORT, () => {  
    console.log(`Server is running at http://localhost:${PORT}`);  
});