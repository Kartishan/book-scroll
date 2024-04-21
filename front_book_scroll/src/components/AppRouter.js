// import {Navigate, Route, Routes} from "react-router-dom";
// import {publicRoutes} from "../routes";
// import {HOME_ROUTE} from "../utils/consts";
//
// const AppRouter = () =>{
//     return(
//         <Routes>
//             {publicRoutes.map(({path, Component}) =>
//                 <Route path={path} element={<Component/>} exact/>
//             )}
//             <Route path='*' element={<Navigate to={HOME_ROUTE}/>}/>
//         </Routes>
//     );
// };
//
// export default AppRouter;

import { Navigate, Route, Routes } from "react-router-dom";
import { publicRoutes } from "../routes";
import { HOME_ROUTE } from "../utils/consts";

const AppRouter = () => {
    return (
        <Routes>
            {publicRoutes.map(({ path, Component }, index) => ( // добавляем индекс вторым параметром в map
                <Route key={index} path={path} element={<Component />} exact /> // используем индекс как ключ
            ))}
            <Route path='*' element={<Navigate to={HOME_ROUTE} />} />
        </Routes>
    );
};

export default AppRouter;
