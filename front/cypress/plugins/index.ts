// cypress/plugins/index.ts
// eslint-disable-next-line @typescript-eslint/no-var-requires
const registerCodeCoverageTasks = require('@cypress/code-coverage/task');

export default (on: any, config: any) => {
  registerCodeCoverageTasks(on, config);
  return config;
};
