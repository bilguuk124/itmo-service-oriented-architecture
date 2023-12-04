import React from 'react';
import { Space, Table, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import Flat from '../model/Flat';
import Column from 'antd/es/table/Column';

interface MyProps{
  flats: Flat[]
}

export default class FlatsTable extends React.Component<MyProps, any, any> {
  private columns: ColumnsType<Flat> = [
    {
      title: 'id',
      dataIndex: 'id',
      key: 'id',
      align: 'center'
    },
    {
      title: 'name',
      dataIndex: 'name',
      key: 'name',
      align: 'center'
    },
    {
      title: 'coordinates',
      children: [
        {
          title: 'X',
          dataIndex: 'coordinatesX',
          key: 'coordinatesX',
          width: '2vh',
          render: (_, rec, __) => rec.coordinates.x
        },
        {
          title: 'Y',
          dataIndex: 'coordinatesY',
          key: 'coordinatesY',
          width: '2vh',
          render: (_, rec, __) => rec.coordinates.y
        }
      ]
    },
    {
      title: 'creationDate',
      dataIndex: 'creationDate',
      key: 'creationDate',
      render: (_, record: Flat, __) => record.creationDate.toLocaleString()
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
      key: 'furnish'
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
    super({flats: data});
  }

  public render(): React.ReactNode {
    return <Table bordered showHeader size='small' columns={this.columns} dataSource={this.props.flats}>
      <Column key={}
    </Table>
  }

}
