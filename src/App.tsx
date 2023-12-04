import React from 'react';
import { Layout, Space } from 'antd';
import FlatsTable from './components/FlatsTable';
import Flat, { Furnish, View, Transport } from './model/Flat';

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

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

const data: Flat[] = [
  {
    id: 1, name: 'name', coordinates: { x: 1, y: 2 },
    creationDate: new Date(), area: 10, roomsNumber: 5,
    furnish: Furnish.FINE, view: View.STREET,
    transport: Transport.LITTLE,
    house: { name: 'My House', year: 100, numberOfFloors: 20 }
  }
];

const App: React.FC = () => (
  <Space direction="vertical" style={{ width: '100%' }} size={[0, 48]}>
    <Layout>
      <Header style={headerStyle}>Header</Header>
      <Content style={contentStyle}>
        <FlatsTable flats={data} />
      </Content>
      <Footer style={footerStyle}>Footer</Footer>
    </Layout>
  </Space>
);


export default App;
