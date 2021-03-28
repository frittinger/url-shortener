import React from 'react';
import axios from 'axios';
import './App.css';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      id: '',
      originalUrl: '',
      shorteningCount: 0,
      redirectCount: 0,
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    this.setState({originalUrl: event.target.value});
  }

  handleSubmit(event) {
  
    axios({
      method: 'post',
      url: 'http://localhost:8080/s/shorturls',
      data: {
        id: '',
        originalUrl: this.state.originalUrl,
        shorteningCount: 0,
        redirectCount: 0,
      }
    }).then(res => this.setState({ id: res.data.id, shorteningCount: res.data.shorteningCount, redirectCount: res.data.redirectCount }));
  
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          URL (e.g. http://www.schnegg.net/test?param=foo):
          <input type="text" value={this.state.originalUrl} onChange={this.handleChange} size="100" />
        </label>
        <input type="submit" value="Shorten" />
        <p>Shortened URL: http://localhost:8080/s/{this.state.id}</p>
        <p>#shortening: {this.state.shorteningCount}</p>
        <p>#redirecting: {this.state.redirectCount}</p>
        
      </form>
    );
  }
}

export default App;

