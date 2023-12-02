import React from 'react';
import { Layout, Space } from 'antd';

const { Header, Footer, Content } = Layout;

const headerStyle: React.CSSProperties = {
  textAlign: 'center',
  margin: 0,
  padding: 0,
  color: '#fff',
  height: '64px',
  paddingInline: 50,
  lineHeight: '64px',
  backgroundColor: '#7dbcea',
};

const contentStyle: React.CSSProperties = {
  margin: 0,
  padding: 0,
  textAlign: 'center',
  minHeight: 20,
  lineHeight: '120px',
  backgroundColor: '#108ee9',
};

const footerStyle: React.CSSProperties = {
  margin: 0,
  padding: 0,
  textAlign: 'center',
  color: '#fff',
  backgroundColor: '#7dbcea',
};

const App: React.FC = () => (
  <Space direction="vertical" style={{ width: '100%' }} size={[0, 48]}>
    <Layout>
      <Header style={headerStyle}>Header</Header>
      <Content style={contentStyle}>Content</Content>
      <Footer style={footerStyle}>Footer</Footer>
    </Layout>
  </Space>
);


export default App;
