function YearPublished ({year}) {

  if(year == 6000){ //Remember, JSON returns results as a string
    return (<>Year Published Not Entered</>)
  }
  
  return (<>{year}</>)
  }
  
  export default YearPublished;