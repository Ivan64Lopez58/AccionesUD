import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { CandlestickSeries, createChart, IChartApi, ISeriesApi } from 'lightweight-charts';
import { AccionHistorica } from '../servicio/acciones/order.service';

@Component({
  selector: 'app-grafica',
  templateUrl: './grafica.component.html',
  styleUrls: ['./grafica.component.css']
})
export class GraficaComponent implements OnInit, AfterViewInit {

  @ViewChild('chartContainer', { static: false }) chartContainer!: ElementRef;

  @Input() chartInicialData!: AccionHistorica[];

  private chart!: IChartApi;
  private series!: ISeriesApi<'Candlestick'>;

  constructor() { }

  ngOnInit() {

  

  }

  ngAfterViewInit() {
    this.initializeChart();
  }

  initializeChart() {
    const chartOptions = { 
      width: 780,
      height: 300,
      layout: { 
        background: 
        { 
          color: 'black' 
        },
        textColor: '#ffffff'
      },
      rightPriceScale: { 
        borderColor: '#cccccc',
      },
      grid: {
        vertLines: { color: '#e1e1e1' },
        horzLines: { color: '#e1e1e1' },
      },
      timeScale: {
        borderColor: '#cccccc',
        timeVisible: true,
        secondsVisible: false,
      }, 
    };
    this.chart = createChart(this.chartContainer.nativeElement,chartOptions)
    this.series = this.chart.addSeries(CandlestickSeries)

  }


}
