# AI Mail Go — 项目总规划

## 🎯 产品定位
AI 原生消息平台 — 多模态输入（语音/图片/视频）→ AI 生成结构化邮件 + 智能附件

## 💡 核心创新
语音 + 图片 → AI 理解 → 自动生成邮件正文 + 结构化附件 + 可执行动作

## 🏗️ 技术栈
- 后端：Java Spring Boot 3.4
- 前端：React (Next.js 15)
- AI：Spring AI + OpenAI GPT-4o + Whisper（语音转文字）
- 数据库：PostgreSQL + pgvector
- 多模态：GPT-4V（图片理解）+ Whisper（语音）
- 部署：腾讯云 + Vercel

## 📋 开发阶段

### Phase 1：核心后端（当前）
- [x] AMP 消息协议设计
- [x] AI 意图识别引擎
- [x] 邮件模型（Entity/DTO/VO）
- [x] REST API（9个端点）
- [x] 项目结构搭建
- [ ] Spring Boot 编译运行
- [ ] 多模态处理模块（语音→文字、图片→结构化）
- [ ] 智能附件生成器
- [ ] PostgreSQL 接入

### Phase 2：AI 多模态
- [ ] Whisper 语音转文字集成
- [ ] GPT-4V 图片理解集成
- [ ] 发票/名片/截图 → 结构化数据
- [ ] 智能附件生成（报销单/工单/档案）
- [ ] 语音+图片混合输入

### Phase 3：前端
- [ ] Next.js 项目搭建
- [ ] 邮件收件箱界面
- [ ] 语音录制组件
- [ ] 图片上传组件
- [ ] AI 建议面板 + Go 按钮

### Phase 4：部署上线
- [ ] Docker 容器化
- [ ] 腾讯云后端部署
- [ ] Vercel 前端部署
- [ ] 域名 + SSL
- [ ] 用户测试

## 📁 项目文件
- 设计方案：projects/aether-mail/DESIGN.md（需更新品牌名）
- 后端代码：projects/aimailgo/
- 学习笔记：learning/java/

## 🔑 关键决策
1. 不是 Gmail 插件，是全新 AI 通信系统
2. 核心卖点：多模态输入 → 智能附件
3. 第一个 MVP 场景：语音报销（语音+拍发票→报销邮件）
4. 先 Agent SDK，再企业版，再个人版
