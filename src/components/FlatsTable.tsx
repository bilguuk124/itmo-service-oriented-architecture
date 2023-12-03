import React from 'react';
import { Space, Table, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import Flat from '../model/Flat';

export default class FlatsTable extends React.Component {
  private columns: ColumnsType<Flat> = [
    {
      title: 'id',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'coordinates',
      dataIndex: 'coordinates',
      key: 'coordinates',
    },
    {
      title: 'creationDate',
      dataIndex: 'creationDate',
      key: 'creationDate',
    },
    {
      title: 'area',
      dataIndex: 'area',
      key: 'area',
    },
    {
      title: 'roomsNumber',
      dataIndex: 'roomsNumber',
      key: 'roomsNumber',
    },
    {
      title: 'furnish',
      dataIndex: 'furnish',
      key: 'furnish',
    },
    {
      title: 'view',
      dataIndex: 'view',
      key: 'view',
    },
    {
      title: 'transport',
      dataIndex: 'transport',
      key: 'transport',
    },
    {
      title: 'house',
      dataIndex: 'house',
      key: 'house',
    },
  ];

  constructor(public data: Flat[]){
    super({data});
  }

  render(): React.ReactNode {
    return <Table columns={this.columns} dataSource={this.data}/>
  }

}


// const data: DataType[] = [
//   {
//     key: '1',
//     name: 'John Brown',
//     age: 32,
//     address: 'New York No. 1 Lake Park',
//     tags: ['nice', 'developer'],
//   },
//   {
//     key: '2',
//     name: 'Jim Green',
//     age: 42,
//     address: 'London No. 1 Lake Park',
//     tags: ['loser'],
//   },
//   {
//     key: '3',
//     name: 'Joe Black',
//     age: 32,
//     address: 'Sydney No. 1 Lake Park',
//     tags: ['cool', 'teacher'],
//   },
// ];

// const FlatsTable = <Table columns={columns} dataSource={data} />;
